package neuberfran.com.jfran.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import neuberfran.com.jfran.model.FireFran
import neuberfran.com.jfran.model.FireFranB

class FireRepositoryB private constructor() {

    private var mFirestore: FirebaseFirestore

    private val _changeGpioB = MutableLiveData<Boolean>()

    val changeGpioB: LiveData<Boolean>
        get() = _changeGpioB

    init {
        _changeGpioB.value = false
    }
    init {
        mFirestore = FirebaseFirestore.getInstance()
    }

    val firefransb: MutableLiveData<List<FireFranB>>
        get() {
            val liveFireFransB = MutableLiveData<List<FireFranB>>()

            mFirestore.collection(FireFranB.COLLECTION)
                .whereEqualTo(
                    FireFranB.FIELD_userId, mFirebaseAuth!!.uid
                )
                .orderBy("position", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e)
                        return@addSnapshotListener
                    }
                    val firefransb = ArrayList<FireFranB>()
                    if (snapshot != null && !snapshot.isEmpty) {
                        for (documentSnapshot in snapshot.documents) {
                            var firefranb = documentSnapshot.toObject(FireFranB::class.java)
                            firefranb!!.id = documentSnapshot.id
                            firefransb.add(firefranb)
                        }
                    }
                    liveFireFransB.postValue(firefransb)
                }
            return liveFireFransB
        }

    fun getFireFranByIdB(firefranIdB: String): MutableLiveData<FireFranB> {
        val liveProjectB = MutableLiveData<FireFranB>()

        val docRef = mFirestore.collection(FireFranB.COLLECTION).document(firefranIdB)
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener //docRef.addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                var firefranb = snapshot.toObject(FireFranB::class.java)
                firefranb!!.id = snapshot.id
                liveProjectB.postValue(firefranb)
            } else {
                Log.d(TAG, "Current data: null")
            }
        }

        return liveProjectB
    }
    fun changeValueGpioGarage() {
        var garagemDocument = mFirestore.collection(FireFranB.COLLECTION)
            .document("garagem")
        GlobalScope.launch(Dispatchers.IO) {
            //      garagemDocument.set(peter).await()
            var gpioGarage = garagemDocument.get().await()
                .toObject(FireFranB::class.java)?.value?.get("openPercent")
            withContext(Dispatchers.Main) {

                if (gpioGarage == 0) {
                    mFirestore.collection(FireFranB.COLLECTION).document("garagem")
                        .update(mapOf("gpiogaragestate" to true))
                    mFirestore.collection(FireFranB.COLLECTION).document("garagem")
                        .update(mapOf("value.openPercent" to 100))
                }else{
                    mFirestore.collection(FireFranB.COLLECTION).document("garagem")
                        .update(mapOf("gpiogaragestate" to true))
                    mFirestore.collection(FireFranB.COLLECTION).document("garagem")
                        .update(mapOf("value.openPercent" to 0))
                }
            }
        }
    }
    // cada documento vira uma FireFran lista
    fun saveFireFranB(firefranb: FireFranB): String {
        val document: DocumentReference
        if (firefranb.id != null) {
            document = mFirestore.collection(FireFranB.COLLECTION).document(firefranb.id!!)
        } else {
            firefranb.userId = mFirebaseAuth!!.uid
            document = mFirestore.collection(FireFranB.COLLECTION).document()
        }
        document.set(firefranb)
        return document.id
    }
    companion object {
        private val TAG = "FireRepositoryB"
        private var mFirebaseAuth: FirebaseAuth? = null
        private var instance: FireRepositoryB? = null
        @Synchronized
        fun getInstance(): FireRepositoryB {
            if (instance == null) {
                instance = FireRepositoryB()
                mFirebaseAuth = FirebaseAuth.getInstance()
            }
            return instance as FireRepositoryB
        }
    }
}