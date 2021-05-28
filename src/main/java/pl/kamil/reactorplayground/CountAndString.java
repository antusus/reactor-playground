package pl.kamil.reactorplayground;

//Excerpt From: Josh Long. “Reactive Spring”.
record CountAndString(long count) {

  public String message() {
    return "# " + this.count;
  }
}
