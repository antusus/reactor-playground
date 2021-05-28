package pl.kamil.reactorplayground;

record CountAndString(long count) {

  public String message() {
    return "# " + this.count;
  }
}
