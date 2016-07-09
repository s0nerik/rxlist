# rxlist
[![](https://jitpack.io/v/s0nerik/rxlist.svg)](https://jitpack.io/#s0nerik/rxlist)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-rxlist-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/3865)

Reactive List implementation using RxJava.

`java.util.List` implementation that can notify of the events that happened with the list using Observable.
This library doesn't have any dependencies except RxJava.

## Usage
You can use an `RxList` either as a wrapper around any other `java.util.List` implementation or as a standalone object
(in this case an `ArrayList` will be used as an implementation for a backing list).
Also it's possible to enable the "item removed" notification for each item that was removed when the list gets cleared (disabled by default).

```java
// ArrayList will be used as a backing list implementation.
RxList<Item> items = new RxList<>();

// Will notify of each removed item when the list is cleared.
RxList<Item> items = new RxList<>(true);

// In this case the RxList will wrap the given list and enable the notifications on it.
// Note: to get notified of the events you must interact with the RxList, not the wrapped list.
List<Item> wrappedItems = new LinkedList<Item>();
RxList<Item> items = new RxList<>(wrappedItems);

// Will notify of each removed item when the list is cleared.
List<Item> wrappedItems = new LinkedList<Item>();
RxList<Item> items = new RxList<>(wrappedItems, true);
```

After you've created the `RxList` instance you will be able to get notified of the events that happen with it.
Use the `events()` method of the `RxList` to get the Observable of the `RxList`'s events:
```java
RxList<Item> items = new RxList<>();
items.events().subscribe(event -> System.out.println(event.type))
```
This way you can get notified of `ITEM_ADDED`, `ITEM_REMOVED`, `ITEM_CHANGED`, `ITEMS_CLEARED` events.

## Usecases
`RxList` will be really useful in situations where you need to keep track of the list state
and react on it's changes somehow. One of the good examples of such situation (from Android development)
is the necessity to call `RecyclerView.Adapter`'s `notifyItem(Inserted/Removed/Changed)` whenever you want
the changes in a list be shown to the user. Take a look at the
[rxlist-binder](https://github.com/s0nerik/rxlist-binder) for a ready to use `RxList` to `RecyclerView.Adapter` binding solution.

### Installation
- To use this library, add the following to your project level `build.gradle`:
```gradle
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```
- Add this to your app's `build.gradle`:
```gradle
compile 'com.github.s0nerik:rxlist:{latest version}'
```

### License

```
Copyright 2016 Alex Isaienko (s0nerik)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
