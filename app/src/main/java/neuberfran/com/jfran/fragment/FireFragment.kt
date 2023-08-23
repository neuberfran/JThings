package neuberfran.com.jfran.fragment

import android.content.Context
import android.os.Bundle
import android.view.ActionMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import neuberfran.com.jfran.databinding.IotEstadoBinding
import neuberfran.com.jfran.viewmodel.FireViewModel

class FireFragment : Fragment() {

 //   private var iotestadoViewModel :FireViewModel? = null

    lateinit var binding  :IotEstadoBinding
    lateinit var bindingB :IotEstadoBinding

    override fun onCreateView(
        inflater :LayoutInflater , container :ViewGroup? ,
        savedInstanceState :Bundle?
    ) :View? {
        var iotestadoViewModel = ViewModelProviders.of(this).get(FireViewModel::class.java)
        var iotestadoViewModelB= ViewModelProviders.of(this).get(FireViewModel::class.java)
        binding  = IotEstadoBinding.inflate(inflater , container , false)
        bindingB = IotEstadoBinding.inflate(inflater , container , false)

        iotestadoViewModel.getFireFranById("alarme2").observe(viewLifecycleOwner , Observer { firefranb ->
            if (firefranb != null) {
                //       productAdapter?.setdevice-configs(device-configs)
                binding.setLifecycleOwner(getActivity())
                //          productAdapter?.setdevice-configs(device-configs)
                binding.viewmodel = iotestadoViewModel
            }
        })

        iotestadoViewModel.changeGpio.observe(viewLifecycleOwner, Observer {
            //       Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })

        iotestadoViewModelB.getFireFranByIdB("garagem2").observe(viewLifecycleOwner , Observer { firefranb ->
            if (firefranb != null) {
                //       productAdapter?.setdevice-configs(device-configs)
                bindingB.setLifecycleOwner(getActivity())
                //          productAdapter?.setdevice-configs(device-configs)
                bindingB.viewmodel = iotestadoViewModelB
            }
        })

        iotestadoViewModelB.changeGpioB.observe(viewLifecycleOwner, Observer {
            //       Toast.makeText(activity, it, Toast.LENGTH_SHORT).show()
        })

        hideKeyboard()  // i don't no why?

        return binding.root
    }

    override fun onResume() {
        super.onResume()
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