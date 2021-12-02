package com.gb.weatherapp.framework.ui.contacts

import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.gb.weatherapp.R
import com.gb.weatherapp.databinding.FragmentContactsBinding

class ContactsFragment : Fragment() {
    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!

    private val permissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            //отправляем callback и получем result
            if (result) {  // если пользователь предоставит разрешение (true)  по запрашиваем контакты
                getContacts()
            } else {
                Toast.makeText( // если нет, то показываем тост с просьбой разрешить
                    context,
                    getString(R.string.need_permissions_to_read_contacts),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission() //проверяем предоставлено ли разрешение на чтение контактов
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun checkPermission() {
        context?.let { notNullContext ->//пробуем получить контекст
            when (PackageManager.PERMISSION_GRANTED) {
                //сверяем с тем что вернется, Должно вернуться 1
                ContextCompat.checkSelfPermission(
                    notNullContext,
                    Manifest.permission.READ_CONTACTS
                ) -> {
                    //Доступ к контактам на телефоне есть, читаем контакты
                    getContacts()
                }
                else -> {
                    //Доступа к контактам  на телефоне нет, запрашиваем разрешения
                    requestPermission()
                }
            }
        }
    }

    private fun requestPermission() {// запрос на разрешение
        permissionResult.launch(Manifest.permission.READ_CONTACTS)
    }

    private fun getContacts() { //метод который запрашивает контакты
        context?.let { nonNullContext ->
            // Отправляем запрос на получение контактов в контент провайдер контактов и получаем ответ в виде Cursor'а
            //contentResolver данные запрашивает а contentProvider данные предоставляет
            val cursorWithContacts: Cursor? = nonNullContext.contentResolver.query( //
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC" // сортировка по имени и по возростанию
            )

            cursorWithContacts?.let { cursor ->
                for (i in 0..cursor.count) {
                    // Переходим на позицию в Cursor'е
                    if (cursor.moveToPosition(i)) {
                        // Берём из Cursor'а столбец с именем
                        val name =
                            cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
//                        val number =
//                            cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                        addView(name)
//                        addView( name + " "  + number)
                    }
                }
            }
            cursorWithContacts?.close()
        }
    }


    private fun addView(contact: String) = with(binding) {
        containerForContacts.addView(TextView(requireContext()).apply {
            text = contact
            textSize = resources.getDimension(R.dimen.main_container_text_size)
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = ContactsFragment()
    }

}