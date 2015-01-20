package dbnormalizer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Gebruiker
 */
public class InvalidAttributeStringException extends Exception
    {
        String wrongString;
        
        public InvalidAttributeStringException(String wrongString) {
            this.wrongString = wrongString;
        }
        
        @Override
        public String getMessage(){
            return String.format("The inputted attribute string \"%s\" is not in the correct format.", "wrongString");
        }
    }
