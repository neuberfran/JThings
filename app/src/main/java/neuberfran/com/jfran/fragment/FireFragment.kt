package neuberfran.com.jfran.fragment

import android.content.Context
import android.os.Bundle
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.item_product.*
import neuberfran.com.jfran.R
import neuberfran.com.jfran.databinding.IotEstadoBinding

import neuberfran.com.jfran.model.FireFran
import neuberfran.com.jfran.viewmodel.FireViewModel

class FireFragment : Fragment() {

    private var iotestadoViewModel :FireViewModel? = null

    lateinit var binding :IotEstadoBinding

    override fun onCreateView(
        inflater :LayoutInflater , container :ViewGroup? ,
        savedInstanceState :Bundle?
    ) :View? {
        val iotestadoViewModel = ViewModelProviders.of(this).get(FireViewModel::class.java)

        binding = IotEstadoBinding.inflate(inflater , container , false)

        iotestadoViewModel!!.getFireFranById("garagem").observe(viewLifecycleOwner , Observer { firefran ->
            if (firefran != null) {
                //       productAdapter?.setdevice-configs(device-configs)
                binding.setLifecycleOwner(getActivity())
                //          productAdapter?.setdevice-configs(device-configs)
                binding.viewmodel = iotestadoViewModel
            }
        })

        iotestadoViewModel.changeGpio.observe(viewLifecycleOwner, Observer {
     //       Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })

//        iotestadoViewModel.allFireFrans.observe(viewLifecycleOwner, Observer {firefrans ->
//            if (firefrans != null) {
//             //   productAdapter?.setdevice-configs(device-configs)
//
//            }
//        })

        hideKeyboard()  // i don't no why?

        return binding.root
    }

    override fun onResume() {
        super.onResume()

//        iotestadoViewModel!!.alldevice-configs.observe(this , Observer { device-configs ->
//            if (device-configs != null) {
//         //       productAdapter?.setdevice-configs(device-configs)
//                binding.setLifecycleOwner(getActivity())
//                binding.viewmodel = iotestadoViewModel
//            }
//        })
    }

    private fun hideKeyboard() {
        if (activity != null) {
            val imm = activity!!
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            if (imm != null && activity!!.currentFocus != null &&
                activity!!.currentFocus!!.windowToken != null
            ) {
                imm.hideSoftInputFromWindow(activity!!.currentFocus!!.windowToken , 0)
            }
        }
    }

    companion object {
        private val TAG = "FireFragment"
    }
}