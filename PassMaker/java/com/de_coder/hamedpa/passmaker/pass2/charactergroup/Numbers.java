package com.de_coder.hamedpa.passmaker.pass2.charactergroup;
//Developed by Hamedpa

public class Numbers implements CharacterGroup {

    private static final String CHARACTERS = "0123456789";

    @Override
    public char[] getChars() {
        return CHARACTERS.toCharArray();
    }
    
}
