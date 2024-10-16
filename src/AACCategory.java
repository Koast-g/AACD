import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.KVPair;
import edu.grinnell.csc207.util.KeyNotFoundException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Represents the mappings for a single category of items that should be displayed
 *
 * @author Catie Baker & Kostiantyn Tsymbal
 */
public class AACCategory implements AACPage {
  /** name of the category. */
  private String name;

  /** Associative array, where key is a string and value is a string. */
  AssociativeArray<String, String> itemArray;

  /**
   * Creates a new empty category with the given name
   *
   * @param name the name of the category
   */
  public AACCategory(String name) {
    this.name = name;
    this.itemArray = new AssociativeArray<>();
  } // AACCategory(String)

  /**
   * Adds the image location, text pairing to the category
   *
   * @param imageLoc the location of the image
   * @param text the text that image should speak
   */
  public void addItem(String imageLoc, String text) {
    try {
      this.itemArray.set(imageLoc, text);
    } catch (Exception e) {
    } // try/catch
  } // addItem(String, String)

  /**
   * Returns an array of all the images in the category
   *
   * @return the array of image locations; if there are no images, it should return an empty array
   */
  public String[] getImageLocs() {
    String[] imageArray = new String[this.itemArray.size()];
    Iterator<KVPair<String, String>> iterator = this.itemArray.iterator();

    int index = 0;
    while (iterator.hasNext()) {
      KVPair<String, String> pair = iterator.next();
      imageArray[index++] = pair.getKey();
    } //while

    return imageArray;
  } // getImageLocs()

  /**
   * Returns the name of the category
   *
   * @return the name of the category
   */
  public String getCategory() {
    return this.name;
  } // getCategory()

  /**
   * Returns the text associated with the given image in this category
   *
   * @param imageLoc the location of the image
   * @return the text associated with the image
   * @throws NoSuchElementException if the image provided is not in the current category
   */
  public String select(String imageLoc) {
    try {
      return this.itemArray.get(imageLoc);
    } catch (KeyNotFoundException e) {
      throw new NoSuchElementException("The image provided is not in the caurrent catagory");
    } // try/catch
  } // select(String)

  /**
   * Determines if the provided images is stored in the category
   *
   * @param imageLoc the location of the category
   * @return true if it is in the category, false otherwise
   */
  public boolean hasImage(String imageLoc) {
    return itemArray.hasKey(imageLoc);
  } // hasImage(String)
} // AACCategory class
