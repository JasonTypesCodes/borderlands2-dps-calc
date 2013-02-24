jQuery(document).ready(function(){
	
  var gunCounter = 1;
  var endpoint = "dps.php";
  
  disableForm();
  setUpCheckBoxes();
  loadFormDefaults();

  getInputFromName("formSubmit").click(function(){
    updateStatus("Calculating...");
    var myData = $('#dpsForm').serialize();
    disableForm();
    jQuery.ajax(endpoint,{
      url: endpoint,
      crossDomain: true,
      type: "GET",
      data: myData,
      error: function(msg){
        var json = $.parseJSON(msg.responseText);
        if(json.message){
          updateMessage(json.message);
        }
        updateStatus("Error!");
        enableForm();
      },
      success: function(msg){
        if(msg.result){
          var name = getInputFromName("name").val();
          addResultListItem(name, msg.result);
        }
        updateStatus("Success!");
        loadFormDefaults();
        enableForm();
      },
    });
  });

  enableForm();
  updateStatus("Ready!");

  
  function addResultListItem(name, dps){
    $('#resultList').append("<li>" + name + ": " + dps + "</li>");
  }

  function setUpCheckBoxes(){
    $('[type|="checkbox"]').each(function(){
      $(this).change(function(){
        var myHiddenInput = getInputFromName($(this).attr("name").replace("_Fake",""));
        
        if($(this).is(':checked')){
          $(myHiddenInput).val("true");
        } else {
          $(myHiddenInput).val("false");
        }
      });
    });
  }

  function disableForm(){
    $("#dpsForm :input").attr("disabled", true);
  }

  function enableForm(){
    $("#dpsForm :input").attr("disabled", false);
  }

	function autoNameGun(){
		getInputFromName("name").val("Some Gun #" + gunCounter);
		gunCounter++;
	}

  function updateMessage(value){
    slideyUpdate("#message", value);
  }

  function updateStatus(value){
    slideyUpdate("#status",value);
  }

  function slideyUpdate(selector, value){
    $(selector).slideUp('fast', function(){
      $(selector).text(value);
      $(selector).slideDown();
    });  
  }

  function loadFormDefaults(){
    $('#dpsForm input').each(function(){
      if(!$(this).is('[type="button"]')){
        $(this).val("");
      }
      if($(this).is('[type="hidden"]')){
        $(this).val("false");
      }
    });
    autoNameGun();
  }

  function getInputFromName(name){
    return $('input[name="' + name + '"]');
  }


});