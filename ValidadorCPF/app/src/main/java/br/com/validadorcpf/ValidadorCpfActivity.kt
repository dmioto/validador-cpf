package br.com.validadorcpf

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.validador_cpf_check.*


class ValidadorCPF : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_validador_cpf)

        setMaskCpf(Mask())
        verifyCPF()
    }

    private fun getCpfNumber() : String{
        return et_cpf.text.toString()
    }


    private fun setMaskCpf(mask: Mask){

        et_cpf.addTextChangedListener(mask.mask(et_cpf, Mask.FORMAT_CPF))

    }

    private fun verifyCPF() {
        button_check.setOnClickListener {
            if (!checkValidCPF(getCpfNumber())) {
                text_validation.text = ""
                ErrorCPF()
            }
            else {
                text_validation.text = getString(R.string.valid_cpf)
            }
        }
    }


    private fun checkValidCPF(cpf: String): Boolean {

        if (cpf.isEmpty()) return false

        val numbers = arrayListOf<Int>()

        cpf.filter { it.isDigit() }.forEach {
            numbers.add(it.toString().toInt())
        }

        if (numbers.size != 11) return false

        (0..9).forEach { n ->
            val digits = arrayListOf<Int>()
            (0..10).forEach { digits.add(n) }
            if (numbers == digits) return false
        }

        val dv1 = ((0..8).sumBy { (it + 1) * numbers[it] }).rem(11).let {
            if (it >= 10) 0 else it
        }

        val dv2 = ((0..8).sumBy { it * numbers[it] }.let { (it + (dv1 * 9)).rem(11) }).let {
            if (it >= 10) 0 else it
        }

        return numbers[9] == dv1 && numbers[10] == dv2
    }

    private fun ErrorCPF(){
        et_cpf.error = "CPF inválido"
    }

}
