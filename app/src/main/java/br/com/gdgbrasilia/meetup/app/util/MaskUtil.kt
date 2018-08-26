package br.com.happyin.app.util

import android.text.*
import android.widget.EditText
import java.util.*

class MaskUtil {

    companion object {

        private val TAG = "MaskUtil"

        private val maskCNPJ = "##.###.###/####-##"
        private val maskCPF = "###.###.###-##"

        private val phoneSmall = "(##) ####-####"
        private val phoneBig = "(##) #####-####"

        private val maskCep = "#####-###"

        private val maskMoney1 = ",#"
        private val maskMoney2 = ",##"
        private val maskMoney3 = "#,##"
        private val maskMoney4 = "##,##"
        private val maskMoney5 = "###,##"
        private val maskMoney6 = "#.###,##"
        private val maskMoney7 = "##.###,##"
        private val maskMoney8 = "###.###,##"
        private val maskMoney9 = "#.###.###,##"

        private val maskPlate = "###-####"

        fun unmask(s: String): String {
            return s.replace("[.]".toRegex(), "").replace("[-]".toRegex(), "")
                    .replace("[/]".toRegex(), "").replace("[(]".toRegex(), "")
                    .replace("[)]".toRegex(), "").replace(" ".toRegex(), "")
                    .replace(",".toRegex(), "")
        }

        fun isASign(c: Char): Boolean {
            return c == '.' || c == '-' || c == '/' || c == '(' || c == ')' || c == ',' || c == ' '
        }

        fun insert(mask: String, ediTxt: EditText): TextWatcher {
            return object : TextWatcher {
                internal var isUpdating: Boolean = false
                internal var old = ""
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    val str = MaskUtil.unmask(s.toString())
                    var mascara = ""
                    if (isUpdating) {
                        old = str
                        isUpdating = false
                        return
                    }
                    var i = 0
                    for (m in mask.toCharArray()) {
                        if (m != '#' && str.length > old.length) {
                            mascara += m
                            continue
                        }
                        try {
                            mascara += str[i]
                        } catch (e: Exception) {
                            break
                        }

                        i++
                    }
                    isUpdating = true
                    ediTxt.setText(mascara)
                    ediTxt.setSelection(mascara.length)
                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun afterTextChanged(s: Editable) {}
            }
        }

        fun insertCPFNJ(ediTxt: EditText): TextWatcher {
            return object : TextWatcher {
                internal var isUpdating: Boolean = false
                internal var old = ""

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    try {
                        var mask = ""
                        val str = MaskUtil.unmask(s.toString())
                        if (str.length <= 11) {
                            mask = MaskUtil.maskCPF
                        } else {
                            mask = MaskUtil.maskCNPJ
                        }
                        var mascara = ""
                        if (isUpdating) {
                            old = str
                            isUpdating = false
                            return
                        }

                        var index = 0
                        for (i in 0 until mask.length) {
                            val m = mask[i]
                            if (m != '#') {
                                if (index == str.length && str.length < old.length) {
                                    continue
                                }
                                mascara += m
                                continue
                            }

                            try {
                                mascara += str[index]
                            } catch (e: Exception) {
                                break
                            }

                            index++
                        }

                        if (mascara.length > 0) {
                            var last_char = mascara[mascara.length - 1]
                            var hadSign = false
                            while (isASign(last_char) && str.length == old.length) {
                                mascara = mascara.substring(0, mascara.length - 1)
                                last_char = mascara[mascara.length - 1]
                                hadSign = true
                            }

                            if (mascara.length > 0 && hadSign) {
                                mascara = mascara.substring(0, mascara.length - 1)
                            }
                        }

                        isUpdating = true
                        ediTxt.setText(mascara)
                        ediTxt.setSelection(mascara.length)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun afterTextChanged(s: Editable) {}
            }
        }

        fun insertPhone(ediTxt: EditText): TextWatcher {
            return object : TextWatcher {
                internal var isUpdating: Boolean = false
                internal var old = ""

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    val mask: String
                    val str = MaskUtil.unmask(s.toString())

                    if (TextUtils.isEmpty(str))
                        return

                    if (str.length <= 10) {
                        mask = MaskUtil.phoneSmall
                    } else {
                        mask = MaskUtil.phoneBig
                    }
                    var mascara = ""
                    if (isUpdating) {
                        old = str
                        isUpdating = false
                        return
                    }

                    var index = 0
                    for (i in 0 until mask.length) {
                        val m = mask[i]
                        if (m != '#') {
                            if (index == str.length && str.length < old.length) {
                                continue
                            }
                            mascara += m
                            continue
                        }

                        try {
                            mascara += str[index]
                        } catch (e: Exception) {
                            break
                        }

                        index++
                    }

                    if (mascara.length > 0) {
                        var last_char = mascara[mascara.length - 1]
                        var hadSign = false
                        while (isASign(last_char) && str.length == old.length) {
                            mascara = mascara.substring(0, mascara.length - 1)
                            last_char = mascara[mascara.length - 1]
                            hadSign = true
                        }

                        if (mascara.length > 0 && hadSign) {
                            mascara = mascara.substring(0, mascara.length - 1)
                        }
                    }

                    isUpdating = true
                    ediTxt.setText(mascara)
                    ediTxt.setSelection(mascara.length)
                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun afterTextChanged(s: Editable) {}
            }
        }

        fun insertCEP(ediTxt: EditText): TextWatcher {
            return object : TextWatcher {
                internal var isUpdating: Boolean = false
                internal var old = ""

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    var mask = ""
                    val str = MaskUtil.unmask(s.toString())
                    mask = MaskUtil.maskCep
                    var mascara = ""
                    if (isUpdating) {
                        old = str
                        isUpdating = false
                        return
                    }

                    var index = 0
                    for (i in 0 until mask.length) {
                        val m = mask[i]
                        if (m != '#') {
                            if (index == str.length && str.length < old.length) {
                                continue
                            }
                            mascara += m
                            continue
                        }

                        try {
                            mascara += str[index]
                        } catch (e: Exception) {
                            break
                        }

                        index++
                    }

                    if (mascara.length > 0) {
                        var last_char = mascara[mascara.length - 1]
                        var hadSign = false
                        while (isASign(last_char) && str.length == old.length) {
                            mascara = mascara.substring(0, mascara.length - 1)
                            last_char = mascara[mascara.length - 1]
                            hadSign = true
                        }

                        if (mascara.length > 0 && hadSign) {
                            mascara = mascara.substring(0, mascara.length - 1)
                        }
                    }

                    isUpdating = true
                    ediTxt.setText(mascara)
                    ediTxt.setSelection(mascara.length)
                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun afterTextChanged(s: Editable) {}
            }
        }

        fun insertMoney(ediTxt: EditText): TextWatcher {
            return object : TextWatcher {
                internal var isUpdating: Boolean = false
                internal var old = ""

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    var mask = ""
                    val str = MaskUtil.unmask(s.toString())
                    if (str.length == 1) {
                        mask = MaskUtil.maskMoney1
                    } else if (str.length == 2) {
                        mask = MaskUtil.maskMoney2
                    } else if (str.length == 3) {
                        mask = MaskUtil.maskMoney3
                    } else if (str.length == 4) {
                        mask = MaskUtil.maskMoney4
                    } else if (str.length == 5) {
                        mask = MaskUtil.maskMoney5
                    } else if (str.length == 6) {
                        mask = MaskUtil.maskMoney6
                    } else if (str.length == 7) {
                        mask = MaskUtil.maskMoney7
                    } else if (str.length == 8) {
                        mask = MaskUtil.maskMoney8
                    } else if (str.length == 9) {
                        mask = MaskUtil.maskMoney9
                    }
                    var mascara = ""
                    if (isUpdating) {
                        old = str
                        isUpdating = false
                        return
                    }

                    var index = 0
                    for (i in 0 until mask.length) {
                        val m = mask[i]
                        if (m != '#') {
                            if (index == str.length && str.length < old.length) {
                                continue
                            }
                            mascara += m
                            continue
                        }

                        try {
                            mascara += str[index]
                        } catch (e: Exception) {
                            break
                        }

                        index++
                    }

                    if (mascara.length > 0) {
                        var last_char = mascara[mascara.length - 1]
                        var hadSign = false
                        while (isASign(last_char) && str.length == old.length) {
                            mascara = mascara.substring(0, mascara.length - 1)
                            last_char = mascara[mascara.length - 1]
                            hadSign = true
                        }

                        if (mascara.length > 0 && hadSign) {
                            mascara = mascara.substring(0, mascara.length - 1)
                        }
                    }

                    isUpdating = true
                    ediTxt.setText(mascara)
                    ediTxt.setSelection(mascara.length)
                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

                override fun afterTextChanged(s: Editable) {}
            }
        }

        fun insertPlate(editText: EditText): TextWatcher {
            return object : TextWatcher {
                internal var after: Int = 0

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                    this.after = after
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable) {
                    editText.error = null
                    if (s.length < 3 && editText.inputType == InputType.TYPE_CLASS_NUMBER) {
                        editText.filters = arrayOf(InputFilter.AllCaps(), InputFilter.LengthFilter(8))
                        editText.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
                    }
                    if (s.length == 3 && after > 0) {
                        editText.inputType = InputType.TYPE_CLASS_NUMBER
                        editText.setText(s.toString() + "-")
                        editText.setSelection(editText.text.toString().length)
                    } else if (s.length == 4) {
                        if (!s.toString().endsWith("-")) {
                            var str = s.toString()
                            str = str.substring(0, 3)
                            str = str + "-" + s.toString()[3]
                            editText.setText(str)
                            editText.setSelection(editText.text.toString().length)
                        }
                    }
                }
            }
        }

        fun insertCardValidThru(editText: EditText): TextWatcher {
            return object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    var working = s.toString()
                    var isValid = true
                    if (working.length == 2 && before == 0) {
                        if (Integer.parseInt(working) < 1 || Integer.parseInt(working) > 12) {
                            isValid = false
                        } else {
                            working += "/"
                            editText.setText(working)
                            editText.setSelection(working.length)
                        }
                    } else if (working.length == 7 && before == 0) {
                        val enteredYear = working.substring(3)
                        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                        if (Integer.parseInt(enteredYear) < currentYear) {
                            isValid = false
                        }
                    } else if (working.length != 7) {
                        isValid = false
                    }

                    if (!isValid) {
                        editText.error = "Enter a valid date: MM/YYYY"
                    } else {
                        editText.error = null
                    }
                }

                override fun afterTextChanged(s: Editable) {

                }
            }
        }
    }
}