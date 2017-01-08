package com.hydroLibCreator.exception;

/**
 * Created by SuperMata on 2016/12/23.
 */
public class ApplicationException extends RuntimeException{
    String exceptionName;
    String desctription;

   public ApplicationException(String exceptionName, String desctription){

    }


    public String getExceptionName() {
        return exceptionName;
    }

    public void setExceptionName(String exceptionName) {
        this.exceptionName = exceptionName;
    }

    public String getDesctription() {
        return desctription;
    }

    public void setDesctription(String desctription) {
        this.desctription = desctription;
    }
}
