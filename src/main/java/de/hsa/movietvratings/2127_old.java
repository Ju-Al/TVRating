             public void onClick(@NonNull View view) {
                 CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
                 customTabsIntent.intent.putExtra("android.intent.extra.REFERRER", Uri.parse("android-app://" + getActivity().getPackageName()));
-                CustomTabActivityHelper.openCustomTab(getActivity(), customTabsIntent, Uri.parse("http://world.openfoodfacts.org/nutrient-levels"), new WebViewFallback());
             }
         };