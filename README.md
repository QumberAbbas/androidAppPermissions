Android Application Permissions Manager
Use this library to manage permissions on runtime

Checking Permissions
  when {
                AppPermissionManager.get().isLocationGranted() -> //do whatever is required
                AppPermissionManager.get().neverAskForLocation(FirstActivity@this) -> //user has tapped not ask again
                else -> disposables.add(AppPermissionManager.get().requestLocationPermission().subscribe(::update, ::failure))
            }
