package edu.grinnell.csc207.util;

import static java.lang.reflect.Array.newInstance;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A basic implementation of Associative Arrays with keys of type K and values of type V.
 * Associative Arrays store key/value pairs and permit you to look up values by key.
 *
 * @param <K> the key type
 * @param <V> the value type
 * @author Your Name Here
 * @author Samuel A. Rebelsky
 */
public class AssociativeArray<K, V> implements Iterable{
  // +-----------+---------------------------------------------------
  // | Constants |
  // +-----------+

  /** The default capacity of the initial array. */
  static final int DEFAULT_CAPACITY = 16;

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /** The size of the associative array (the number of key/value pairs). */
  int size;

  /** The array of key/value pairs. */
  KVPair<K, V>[] pairs;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /** Create a new, empty associative array. */
  @SuppressWarnings({"unchecked"})
  public AssociativeArray() {
    // Creating new arrays is sometimes a PITN.
    this.pairs = (KVPair<K, V>[]) newInstance((new KVPair<K, V>()).getClass(), DEFAULT_CAPACITY);
    this.size = 0;
  } // AssociativeArray()

  // +------------------+--------------------------------------------
  // | Standard Methods |
  // +------------------+

  /**
   * Create a copy of this AssociativeArray.
   *
   * @return a new copy of the array
   */
  public AssociativeArray<K, V> clone() {
    AssociativeArray<K, V> clonedArray = new AssociativeArray<K, V>();
    try {
      // loop through each value of this array and set these value to a new array
      for (KVPair<K, V> pair : pairs) {
        clonedArray.set(pair.key, pair.val);
      } // for
      // set the size of a new array
      clonedArray.size = this.size;
    } catch (Exception e) {
    } // try/catch
    return clonedArray;
  } // clone()

  /**
   * Convert the array to a string.
   *
   * @return a string of the form "{Key0:Value0, Key1:Value1, ... KeyN:ValueN}"
   */
  public String toString() {
    String output = "{";
    // putting the string together
    for (int i = 0; i < this.size; i++) {
      if (i != this.size - 1) {
        output += this.pairs[i].key + ":" + this.pairs[i].val + ", ";
      } else {
        output += this.pairs[i].key + ":" + this.pairs[i].val;
      } // if
    } // end of for
    return output + "}";
  } // toString()

  // +----------------+----------------------------------------------
  // | Public Methods |
  // +----------------+

  /**
   * Set the value associated with key to value. Future calls to get(key) will return value.
   *
   * @param key The key whose value we are seeting.
   * @param value The value of that key.
   * @throws NullKeyException If the client provides a null key.
   */
  public void set(K key, V value) throws NullKeyException {
    // key is null we throw an exception
    if (key == null) {
      throw new NullKeyException("Null key provided");
    } // if
    try {
      // replace the exciting key with different value
      if (hasKey(key)) {
        this.pairs[find(key)].val = value;
      } else {
        // expanding an array if needed
        if (this.size >= this.pairs.length) {
          expand();
        } // if
        // pointing to a new pair
        this.pairs[this.size] = new KVPair<>(key, value);
        this.size++;
      } // if
    } catch (Exception e) {
    } // try/catch
  } // set(K,V)

  /**
   * Get the value associated with key.
   *
   * @param key A key
   * @return The corresponding value
   * @throws KeyNotFoundException when the key is null or does not appear in the associative array.
   */
  public V get(K key) throws KeyNotFoundException {
    try {
      return this.pairs[find(key)].val;
    } catch (Exception e) {
      throw new KeyNotFoundException("Key is null or does not appear in the associative array");
    } // try/catch
  } // get(K)

  /**
   * Determine if key appears in the associative array. Should return false for the null key, since
   * it cannot appear.
   *
   * @param key The key we're looking for.
   * @return true if the key appears and false otherwise.
   */
  public boolean hasKey(K key) {
    try {
      find(key);
      return true;
    } catch (KeyNotFoundException e) {
      return false;
    } // try/catch
  } // hasKey(K)

  /**
   * Remove the key/value pair associated with a key. Future calls to get(key) will throw an
   * exception. If the key does not appear in the associative array, does nothing.
   *
   * @param key The key to remove.
   */
  public void remove(K key) {
    if (hasKey(key)) {
      try {
        this.pairs[find(key)] = this.pairs[this.size - 1];
        this.size--;
        this.pairs[this.size] = null;
      } catch (Exception e) {
      } // try/catch
    } // if
  } // remove(K)

  /**
   * Determine how many key/value pairs are in the associative array.
   *
   * @return The number of key/value pairs in the array.
   */
  public int size() {
    return this.size;
  } // size()

  // +-----------------+---------------------------------------------
  // | Private Methods |
  // +-----------------+

  /** Expand the underlying array. */
  void expand() {
    this.pairs = java.util.Arrays.copyOf(this.pairs, this.pairs.length * 2);
  } // expand()

  /**
   * Find the index of the first entry in `pairs` that contains key. If no such entry is found,
   * throws an exception.
   *
   * @param key The key of the entry.
   * @return The index of the key, if found.
   * @throws KeyNotFoundException If the key does not appear in the associative array.
   */
  int find(K key) throws KeyNotFoundException {
    for (int i = 0; i < this.size; i++) {
      if (this.pairs[i].key.equals(key)) {
        return i;
      } // if
    } // for
    throw new KeyNotFoundException("Key does not appear in the associative array");
  } // find(K)

  // +-----------------+---------------------------------------------
  // | Iterator |
  // +-----------------+
  public Iterator<KVPair<K, V>> iterator(){
    return new Iterator<KVPair<K, V>>() {
      int i = 0;

      @Override
      public boolean hasNext() {
        return i < AssociativeArray.this.size;
      } //hasNext()

      @Override
      public KVPair<K, V> next() throws NoSuchElementException{
        if(!hasNext()){
          throw new NoSuchElementException("there is no such element");
        } // if
        return AssociativeArray.this.pairs[this.i++];
      } //next()
    }; //new Iterator
  } //inretator
} // class AssociativeArray
