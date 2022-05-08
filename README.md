# PaginationBugDemo
This repo demonstrate a bug in `pagination3`:
When updating an item in `room` `db` in an index beyond the `initialLoadSize` and then updating again,
the recylerView `jumps` to a wrong index.

In this screenshot, a user scroll from position 8to position 9, clicks once to toggle the switch, and after toggling again, the `recylerView` is `jumping` 
to index 8.

https://user-images.githubusercontent.com/67230037/167290320-28beadd4-3f06-4517-ac87-3161cd1cef1d.mp4

The repo consist of:
* `MainActivity` with an `ReecylerView` and `PagingDataAdapter`
* `db` of `room` with one `entity`: `Item`
* A `viewModel` to repopulate the `db` and handle `click` events to update the item.
