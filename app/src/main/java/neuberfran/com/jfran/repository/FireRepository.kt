package neuberfran.com.jfran.repository

import android.util.Log
import androidx.constraintlayout.widget.Constraints.TAG
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import neuberfran.com.jfran.model.FireFran
import neuberfran.com.jfran.model.FireFranB

class FireRepository private constructor() {

    private val mFirestore: FirebaseFirestore

    private val _changeGpio = MutableLiveData<Boolean>()

    val changeGpio: LiveData<Boolean>
        get() = _changeGpio

    init {
        _changeGpio.value = false
    }

    init {
        mFirestore = FirebaseFirestore.getInstance()
    }

    val firefrans: MutableLiveData<List<FireFran>>
        get() {
            val liveFireFrans = MutableLiveData<List<FireFran>>()

            mFirestore.collection(FireFran.COLLECTION)
                .whereEqualTo(
                    FireFran.FIELD_userId, mFirebaseAuth!!.uid
                )
                .orderBy("position", Query.Direction.ASCENDING)
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e)
                        return@addSnapshotListener
                    }

                    val firefrans = ArrayList<FireFran>()
                    if (snapshot != null && !snapshot.isEmpty) {
                        for (documentSnapshot in snapshot.documents) {
                            val firefran = documentSnapshot.toObject(FireFran::class.java)
                            firefran!!.id = documentSnapshot.id
                            firefrans.add(firefran)
                        }
                    }
                    liveFireFrans.postValue(firefrans)
                }

            return liveFireFrans
        }

    fun getFireFranById(firefranId: String): MutableLiveData<FireFran> {
        val liveProject = MutableLiveData<FireFran>()

        val docRef = mFirestore.collection(FireFran.COLLECTION).document(firefranId)
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener //docRef.addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                var firefran = snapshot.toObject(FireFran::class.java)
                firefran!!.id = snapshot.id
                liveProject.postValue(firefran)
            } else {
                Log.d(TAG, "Current data: null")
            }
        }

        return liveProject
    }

    fun changeValueGpioAlarm() {

        var alarmeDocument = mFirestore.collection(FireFran.COLLECTION)
            .document("alarme")

        GlobalScope.launch(Dispatchers.IO) {

            var gpioAlarm =
                alarmeDocument.get().await().toObject(FireFran::class.java)?.value?.get("on")
            withContext(Dispatchers.Main) {

                    if (gpioAlarm!!) {
                        mFirestore.collection(FireFran.COLLECTION).document("alarme")
                            .update(mapOf("value.on" to false))
                    }
                    if (!gpioAlarm!!) {
                        mFirestore.collection(FireFran.COLLECTION).document("alarme")
                            .update(mapOf("value.on" to true))
                    }
            }
        }
    }

    // cada documento vira uma FireFran lista
    fun saveFireFran(firefran: FireFran): String {
        val document: DocumentReference
        if (firefran.id != null) {
            document = mFirestore.collection(FireFran.COLLECTION).document(firefran.id!!)
        } else {
            firefran.userId = mFirebaseAuth!!.uid
            document = mFirestore.collection(FireFran.COLLECTION).document()
        }
        document.set(firefran)

        return document.id
    }

    companion object {
        private val TAG = "FireRepository"
        private var mFirebaseAuth: FirebaseAuth? = null

        private var instance: FireRepository? = null

        @Synchronized
        fun getInstance(): FireRepository {
            if (instance == null) {
                instance = FireRepository()
                mFirebaseAuth = FirebaseAuth.getInstance()
            }
            return instance as FireRepository
        }
    }
}