import edu.grinnell.csc207.util.AssociativeArray;
import edu.grinnell.csc207.util.KVPair;
import edu.grinnell.csc207.util.KeyNotFoundException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Creates a set of mappings of an AAC that has two levels, one for categories and then within each
 * category, it has images that have associated text to be spoken. This class provides the methods
 * for interacting with the categories and updating the set of images that would be shown and
 * handling an interactions.
 *
 * @author Catie Baker & Kostiantyn Tsymbal
 */
public class AACMappings implements AACPage {

  /** Associative Array, where key is a string, and value is AACCategory. */
  AssociativeArray<String, AACCategory> array;

  /** current category. */
  AACCategory currentCat;

  /** Default page. */
  AACCategory homepage;

  /** file. */
  File file;

  /**
   * Reads the file, adding catagories to the home page and adding imgalocs
   *
   * @param filename file provided as a string
   */
  private void readFile() throws FileNotFoundException {
    try (Scanner eyes = new Scanner(this.file); ) {
      while (eyes.hasNextLine()) {
        String line = eyes.nextLine().trim();
        String[] tokens = line.split("\\s", 2);
        if (tokens[0].charAt(0) == '>') {
          if (currentCat != null) {
            currentCat.addItem(tokens[0].substring(1).trim(), tokens[1]);
          } // if
        } else {
          currentCat = new AACCategory(tokens[1]);
          try {
            this.array.set(tokens[0], currentCat);
            homepage.addItem(tokens[0], tokens[1]);
          } catch (Exception e) {
            // do nothing
          } // tr/catch
        } // if
      } // while
    } catch (FileNotFoundException e) {
      throw new FileNotFoundException();
    } // try/catch
    currentCat = homepage;
    if (currentCat == null) {
      currentCat = homepage;
    } // if
  } // readFile(String)

  /**
   * Creates a set of mappings for the AAC based on the provided file. The file is read in to create
   * categories and fill each of the categories with initial items. The file is formatted as the
   * text location of the category followed by the text name of the category and then one line per
   * item in the category that starts with > and then has the file name and text of that image
   *
   * <p>for instance: img/food/plate.png food >img/food/icons8-french-fries-96.png french fries
   * >img/food/icons8-watermelon-96.png watermelon img/clothing/hanger.png clothing
   * >img/clothing/collaredshirt.png collared shirt
   *
   * <p>represents the file with two categories, food and clothing and food has french fries and
   * watermelon and clothing has a collared shirt
   *
   * @param filename the name of the file that stores the mapping information
   */
  public AACMappings(String filename) {
    this.array = new AssociativeArray<>();
    this.file = new File(filename);
    this.homepage = new AACCategory("");
    try {
      this.array.set(homepage.getCategory(), homepage);
      readFile();
    } catch (Exception e) {
      // do nothing
    } // try/catch
  } // AACMappings(String)

  /**
   * Given the image location selected, it determines the action to be taken. This can be updating
   * the information that should be displayed or returning text to be spoken. If the image provided
   * is a category, it updates the AAC's current category to be the category associated with that
   * image and returns the empty string. If the AAC is currently in a category and the image
   * provided is in that category, it returns the text to be spoken.
   *
   * @param imageLoc the location where the image is stored
   * @return if there is text to be spoken, it returns that information, otherwise it returns the
   *     empty string
   * @throws NoSuchElementException if the image provided is not in the current category
   */
  public String select(String imageLoc) {
    if (currentCat != homepage && currentCat != null) {
      if (currentCat.hasImage(imageLoc)) {
        return currentCat.select(imageLoc);
      } //if
    } //if
    if(currentCat == homepage){
      if(homepage.hasImage(imageLoc)){
        try {
            currentCat = this.array.get(imageLoc);
            return "";
        } catch (KeyNotFoundException e) {
            // do nothing
        } //try/catch
      } //if
    } //if
  
    throw new NoSuchElementException("image provided is not in the current category");
  } // select(String)

  /**
   * Provides an array of all the images in the current category
   *
   * @return the array of images in the current category; if there are no images, it should return
   *     an empty array
   */
  public String[] getImageLocs() {
    if (currentCat != null) {
      return currentCat.getImageLocs();
    } // if
    return new String[0];
  } // getImageLocs()

  /** Resets the current category of the AAC back to the default category */
  public void reset() {
    this.currentCat = homepage;
  } // reset()

  /**
   * Writes the ACC mappings stored to a file. The file is formatted as the text location of the
   * category followed by the text name of the category and then one line per item in the category
   * that starts with > and then has the file name and text of that image
   *
   * <p>for instance: img/food/plate.png food >img/food/icons8-french-fries-96.png french fries
   * >img/food/icons8-watermelon-96.png watermelon img/clothing/hanger.png clothing
   * >img/clothing/collaredshirt.png collared shirt
   *
   * <p>represents the file with two categories, food and clothing and food has french fries and
   * watermelon and clothing has a collared shirt
   *
   * @param filename the name of the file to write the AAC mapping to
   */
  public void writeToFile(String filename) {
    Iterator<KVPair<String, AACCategory>> catIterator = array.iterator();
    try {
      PrintWriter pen = new PrintWriter(filename);
      while (catIterator.hasNext()) {
        KVPair<String, AACCategory> pair = catIterator.next();
        String catImageLoc = pair.getKey();
        AACCategory cat = pair.getVal();
        if(cat != homepage){
        pen.println(catImageLoc + " " + cat.getCategory());
        // Proccessing individual item
        for (String imageLoc : cat.getImageLocs()) {
          pen.println(">" + imageLoc + " " + cat.select(imageLoc));
        } // for
      }
      } //  while
      pen.close();
    } catch (Exception e) {
      // do nothing
    } // try/catch
  } // writeToFile(String)

  /**
   * Adds the mapping to the current category (or the default category if that is the current
   * category)
   *
   * @param imageLoc the location of the image
   * @param text the text associated with the image
   */
  public void addItem(String imageLoc, String text) {
    if (currentCat != null) {
      if (currentCat == homepage) {
        currentCat.addItem(imageLoc, text);
        AACCategory newCat = new AACCategory(text);
        try {
          this.array.set(imageLoc, newCat);
        } catch (Exception e) {
          // do nothing
        } // try/catch
      } else {
        currentCat.addItem(imageLoc, text);
      } // if
    } // if
  } // addItem(String, String)

  /**
   * Gets the name of the current category
   *
   * @return returns the current category or the empty string if on the default category
   */
  public String getCategory() {
    if (currentCat == homepage) {
      return "";
    } else {
      return currentCat.getCategory();
    } // if
  } // getCategory()

  // /**
  //  * Determines if the provided image is in the set of images that can be displayed and false
  //  * otherwise
  //  *
  //  * @param imageLoc the location of the category
  //  * @return true if it is in the set of images that can be displayed, false otherwise
  //  */
  // public boolean hasImage(String imageLoc) {
  //   Iterator<KVPair<String, AACCategory>> catIterator = array.iterator();
  //   try {
  //     while (catIterator.hasNext()) {
  //       KVPair<String, AACCategory> pair = catIterator.next();
  //       AACCategory cat = pair.getVal();
  //       // Proccessing individual item
  //       Iterator<KVPair<String, String>> itemIterator = cat.itemArray.iterator();
  //       while (itemIterator.hasNext()) {
  //         KVPair<String, String> item = itemIterator.next();
  //         String curImageLoc = item.getKey();
  //         if (curImageLoc == imageLoc) {
  //           return true;
  //         } // if
  //       } // inner while
  //     } // outer while
  //   } catch (Exception e) {
  //   } // tr/catch
  //   return false;
  // } // hasImage(String)
  /**
   * Determines if the provided image is in the set of images that can be displayed and false
   * otherwise
   *
   * @param imageLoc the location of the category
   * @return true if it is in the set of images that can be displayed, false otherwise
   */
  public boolean hasImage(String imageLoc) {
    return currentCat != null && currentCat.hasImage(imageLoc);
  } // hasImage(String)
} // ACC
