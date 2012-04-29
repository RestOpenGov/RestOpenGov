package ar.com.restba.connectors.con;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


class Ite<T> implements Iterator<List<T>> {
    private RestBAIterator<T> connection;
    private boolean initialPage = true;

    /**
     * Creates a new iterator over the given {@code connection}.
     * 
     * @param connection
     *          The connection over which to iterate.
     */
    protected Ite(RestBAIterator<T> connection) {
      this.connection = connection;
    }

    /**
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
      return connection.hasNext();
    }

    /**
     * @see java.util.Iterator#next()
     */
    public List<T> next() {
      // Special case: initial page will always have data, return it
      // immediately.
      if (initialPage) {
        initialPage = false;
        return connection.getData();
      }

      if (!connection.hasNext())
        throw new NoSuchElementException("There are no more pages in the connection.");

      connection = connection.fetchNextPage();
      return connection.getData();
    }

    /**
     * @see java.util.Iterator#remove()
     */
    public void remove() {
      throw new UnsupportedOperationException(Ite.class.getSimpleName()
          + " doesn't support the remove() operation.");
    }
  }