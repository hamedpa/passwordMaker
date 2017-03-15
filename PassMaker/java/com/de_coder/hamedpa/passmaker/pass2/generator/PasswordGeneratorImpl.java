package com.de_coder.hamedpa.passmaker.pass2.generator;

import com.de_coder.hamedpa.passmaker.pass2.charactergroup.CharacterGroup;
//Developed by Hamedpa

import java.security.SecureRandom;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;


public class PasswordGeneratorImpl implements PasswordGenerator {

    @Override
    public String generatePassword(int length, Collection<CharacterGroup> characterGroups) {
        String password = "";
        if (!characterGroups.isEmpty()) {
            char[] chars = flatten(characterGroups);
            password = generatePasswordFromCharacters(chars, length);
        }
        return password;
    }


    private String generatePasswordFromCharacters(char[] chars, int length) {
        Random random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < length; i++) {
            int idx = random.nextInt(chars.length);
            sb.append(chars[idx]);
        }
        return sb.toString();
    }

    private char[] flatten(Collection<CharacterGroup> characterGroups) {
        Iterator<CharacterGroup> iterator = characterGroups.iterator();
        char[] array = iterator.next().getChars();
        while (iterator.hasNext()) {
            array = concat(array, iterator.next().getChars());
        }
        return array;
    }


    private char[] concat(char[] a, char[] b) {
        char[] c = new char[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

}
