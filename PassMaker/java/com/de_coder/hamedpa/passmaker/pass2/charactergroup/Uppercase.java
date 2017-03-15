package com.de_coder.hamedpa.passmaker.pass2.charactergroup;
//Developed by Hamedpa

public class Uppercase implements CharacterGroup {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Override
    public char[] getChars() {
        return CHARACTERS.toCharArray();
    }

}
