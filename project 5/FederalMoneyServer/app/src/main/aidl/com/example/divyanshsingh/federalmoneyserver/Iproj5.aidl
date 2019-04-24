// Iproj5.aidl
package com.example.divyanshsingh.federalmoneyserver;

// Declare any non-default types here with import statements


interface Iproj5 {
    String getMessage(String name);
    	int getResult(int val1, int val2);
List monthlyAvgCash(int year);
List dailyCash(int day,int month,int year,int offset);
    }
