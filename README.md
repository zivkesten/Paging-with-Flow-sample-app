# Paging-with-Flow-sample-app
This is a sample app that demonstrates endless scrolling with the Android Paging 3.0 library.

Add Paging library to your gradle file:

dependencies {
  def paging_version = "3.0.0-alpha07"

  implementation "androidx.paging:paging-runtime:$paging_version"

  // alternatively - without Android dependencies for tests
  testImplementation "androidx.paging:paging-common:$paging_version"

  // optional - RxJava2 support
  implementation "androidx.paging:paging-rxjava2:$paging_version"

  // optional - Guava ListenableFuture support
  implementation "androidx.paging:paging-guava:$paging_version"
}
