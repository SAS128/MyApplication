package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;

public class MainActivity extends Activity {

    //Объявляем использование элемента TextView:
    public TextView contacts;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Связываемся с нашим элементом TextView:
        contacts = (TextView) findViewById(R.id.textView);

        //Метод получения контактных данных
        getContacts();
    }

    //Описываем метод:
    public void getContacts() {

        String phoneNumber = null;

        //Связываемся с контактными данными и берем с них значения id контакта, имени контакта и его номера:
        Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
        String _ID = ContactsContract.Contacts._ID;
        String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
        String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

        Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
        String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;



        StringBuffer output = new StringBuffer();
        ContentResolver contentResolver = getContentResolver();
        @SuppressLint("Recycle") Cursor cursor = contentResolver.query(CONTENT_URI, null,null, null, null);

        //Запускаем цикл обработчик для каждого контакта:
        if (cursor.getCount() > 0) {

            //Если значение имени и номера контакта больше 0 (то есть они существуют) выбираем
            //их значения в приложение привязываем с соответствующие поля "Имя" и "Номер":
            while (cursor.moveToNext()) {
                String contact_id = cursor.getString(cursor.getColumnIndex( _ID ));
                String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));
                int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                //Получаем имя:
                if (hasPhoneNumber > 0) {
                    output.append("\n Имя: ").append(name);
                    @SuppressLint("Recycle") Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null,
                            Phone_CONTACT_ID + " = ?", new String[] { contact_id }, null);

                    //и соответствующий ему номер:
                    while (phoneCursor.moveToNext()) {
                        phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                        output.append("\n Телефон: ").append(phoneNumber);
                    }
                }
                output.append("\n");
            }

            //Полученные данные отображаем с созданном элементе TextView:
            contacts.setText(output);
        }
    }
}