package webshop.core.service;

public class LookupService {
  public static <E extends Enum<E>> boolean isStringEnumValue(Class<E> e, String id) {
    try {
      Enum.valueOf(e, id);

      return true;
    } catch (IllegalArgumentException ex) {
      return false;
    }
  }
}
