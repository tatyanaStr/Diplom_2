package site.nomoreparties.stellarburgers.additional;

public class RandomValue {

    public static String getAlphaNumericString(int n) {
        String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789" + "abcdefghijklmnopqrstuvxyz";
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            int index = (int) (AlphaNumericString.length() * Math.random());
            sb.append(AlphaNumericString.charAt(index));
        }

        return sb.toString();
    }

    public String email(){
        var firstPart = getAlphaNumericString(5);
        return firstPart.toLowerCase() + "@yandex.ru";
    }
    public String password(int i){
        return getAlphaNumericString(i);
    }

    public String name(int i){
        return getAlphaNumericString(i).toLowerCase();
    }
}
