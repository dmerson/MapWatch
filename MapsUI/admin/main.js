requirejs.config({
    paths: {
        'datatables': '//cdn.datatables.net/1.10-dev/js/jquery.dataTables'
    }
});
require([
  "views/adminView",
  "datatables"
  ], function(AppView) {
    //loads resources and starts the app
    app = new AppView({});
});
