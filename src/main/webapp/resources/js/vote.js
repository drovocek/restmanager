var VoteWidget= {
    settings: {
        $counter: $('.vote-count'),
        $btn:     $('.myform button'),
        $btnImg: $('.myform button .icon .fa'),
    },
    init: function() {
        VoteWidget.bind();
    },
    bind: function() {
        VoteWidget.settings.$btn.click(function(){
            if (! $(this).hasClass('complete')) {
                VoteWidget.bumpCount();
            }
            $(this).toggleClass('complete');
            VoteWidget.toggleText();

            return false;
        });
    },
    bumpCount: function() {
        var current_count = $('.vote-count').text();
        count = parseInt(current_count);
        count++;
        // VoteWidget.settings.$counter.toggleClass('bumped').text(count);
    },
    toggleText: function(){
        var imgBtn = VoteWidget.settings.$btnImg;
        console.log(imgBtn.hasClass('fa-heart-o'));
        if ( imgBtn.hasClass('fa-heart-o') ) {
            imgBtn.removeClass('fa-heart-o');
            console.log(imgBtn.hasClass('fa-heart-o'));
            imgBtn.addClass('fa-heart');
            console.log(imgBtn.hasClass('fa-heart'));
        }
        else if(imgBtn.hasClass('fa-heart')){
            imgBtn.removeClass('fa-heart');
            imgBtn.addClass('fa-heart-o');
        }
        // var $text_container = $('.myform button .text');
        // var alt_text = VoteWidget.settings.$btn.data('alt-text');
        // var default_text = VoteWidget.settings.$btn.data('default-text');
        // var current_text = $text_container.text();
        // console.log('current is ' + current_text);
        // console.log($('.myform button .icon .fa').hasClass('fa-heart-o'));
        // console.log($('.myform button .icon .fa').toggleClass('fa-heart-o'));
        // console.log($('.myform button .icon .fa').toggleClass('fa-heart'));
        // if ( current_text == default_text ) {
        //     // $text_container.text(alt_text)
        // } else {
        //     // $text_container.text(default_text)
        // }
        // var $text_container = $('.myform button .text');
        // var alt_text = VoteWidget.settings.$btn.data('alt-text');
        // var default_text = VoteWidget.settings.$btn.data('default-text');
        // var current_text = $text_container.text();
        // console.log('current is ' + current_text);
        // if ( current_text == default_text ) {
        //     $text_container.text(alt_text)
        // } else {
        //     $text_container.text(default_text)
        // }
    }
}


VoteWidget.init();

