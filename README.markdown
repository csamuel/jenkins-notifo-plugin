## Notifo Plugin for Hudson

This is a Hudson plugin for integration with the [Notifo API](http://notifo.com/) to push build notifications to mobile devices.

### Before You Begin

* In order to use the plugin you will need a Notifo service account along with an API token.
* Any user accounts that you wish to send notifications to must be subscribed to the service account. You can do so using the [subscribe console](http://notifo.com/service/console_subscribe).

### Installation

1. git clone git@github.com:csamuel/hudson-notifo-plugin.git
2. cd hudson-notifo-plugin
3. mvn package
4. Upload target/notifo.hpi to your Hudson server. You will need to restart hudson for it to take effect.
	
### TODO:

* Custom URL and labels on notifications
* Unit tests
