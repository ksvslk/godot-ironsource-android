extends Node
# -------------------------------------------
# Available ad units
# -------------------------------------------
const BANNER = "BANNER"
const REWARDED_VIDEO = "REWARDED_VIDEO"
const INTERSTITIAL = "INTERSTITIAL"

# TODO: set your IronSource app ID and ad units here
const APP_KEY = "<INSERT_YOUR_APP_ID>"
onready var ad_units: Array = [BANNER, REWARDED_VIDEO, INTERSTITIAL]

var IronSource = null

func _ready():
	if Engine.has_singleton("IronSourcePlugin"):
		IronSource = Engine.get_singleton("IronSourcePlugin")

# -------------------------------------------
# Initialize
# -------------------------------------------
func init_ads():
	if IronSource:
		IronSource.init(get_instance_id(), ad_units, APP_KEY)

# -------------------------------------------
# Rewarded methods
# -------------------------------------------
func show_rewarded_video(placement_name: String = ""):
	if IronSource:
		IronSource.showRewardedVideo(placement_name)

# -------------------------------------------
# Rewarded video callbacks
# -------------------------------------------
func on_rewarded_video_availability_changed(available: bool):
	print("on_rewarded_video_availability_changed to: " + str(available))

func on_rewarded_video_ad_closed(name: String, amount: int):
	print("on_rewarded_video_ad_closed")
	# TODO: show user the rewards in some way
	print("reward name: " + name + ", " + "amount: " + str(amount))

func on_rewarded_video_ad_show_failed(error: String):
	print("on_rewarded_video_ad_show_failed: " + error)
	
# -------------------------------------------
# Interstitial methods
# -------------------------------------------
func load_interstitial():
	if IronSource:
		IronSource.loadInterstitial()
		
func show_interstitial():
	if IronSource:
		IronSource.showInterstitial()
		
# -------------------------------------------
# Interstitial callbacks
# -------------------------------------------
func on_interstitial_ad_ready():
	print("on_interstitial_ad_ready")

func on_interstitial_ad_show_failed(error: String):
	print("on_interstitial_ad_show_failed: " + error)

func on_interstitial_ad_load_failed(error: String):
	print("on_interstitial_ad_show_failed: " + error)
	
# -------------------------------------------
# Banner methods
# -------------------------------------------
func create_and_load_banner():
	IronSource.createAndloadBanner()

func destroy_and_detach_banner():
	IronSource.destroyAndDetachBanner()
	
# -------------------------------------------
# Banner callbacks
# -------------------------------------------
func on_banner_ad_loaded():
	print("on_banner_ad_loaded")

func on_banner_ad_load_failed(error: String):
	print("on_banner_ad_load_failed: " + error)
