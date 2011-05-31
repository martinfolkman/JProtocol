package org.jprotocol.codegen

class NameFormatter {
    public static String formatName(String name) {
        name = name.replace(' ', '_');
        name = name.replace('/', '_');
        name = name.replace('-', 'M');
        name = name.replace('-', '_');
        name = name.replace('.', '_');
        name = name.replace(',', '_');
        name = name.replace('(', '_');
        name = name.replace(')', '_');
        name = name.replace('+', 'P');
        name = name.replace('*', '_');
        name = name.replace('[', '_');
        name = name.replace(']', '_');
        name = name.replace('=', '_');
        name = name.replace('>', '_');
        name = name.replace('<', '_');
        name = name.replace(':', '_');
        name = name.replace('!', '_');
        name = name.replace('\u2013', '_');
        name = name.replace('%', "_Percent_")
        name = name.replace('&', "_And_")
        name = replace(name, 8482, '_')
        if (name.startsWith("0") || name.startsWith("1") || name.startsWith("2") || name.startsWith("3") ||
            name.startsWith("4") || name.startsWith("5") || name.startsWith("6") || name.startsWith("7") ||
            name.startsWith("8") || name.startsWith("9")) {
            name = "_" + name;
        }
        
        return name;
    }
    def static replace(str, oldChar, newChar) {
        def ix = str.indexOf(oldChar) 
        if (ix < 0) return str
        return str.replace(str.charAt(ix), newChar.charAt(0))
    }


}