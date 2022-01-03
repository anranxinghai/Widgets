//
//  UIViewController+AboutViewController.m
//  DragGame
//
//  Created by 黯然星海 on 2021/12/30.
//  Copyright © 2021年 anranxinghai. All rights reserved.
//

#import "UIViewController+AboutViewController.h"

@implementation AboutViewController
@synthesize webView;


- (void)viewDidLoad {
    [super viewDidLoad];
        NSString * htmlFile = [[NSBundle mainBundle]pathForResource:@"hello" ofType:@"html"];
        NSData * htmlData = [NSData dataWithContentsOfFile:htmlFile];
        NSURL * baseUrl = [NSURL fileURLWithPath:[[NSBundle mainBundle] bundlePath]];
//        NSURL * baseUrl = [NSURL URLWithString:@"http://www.apple.com"];
//        NSURLRequest *request = [NSURLRequest requestWithURL:baseUrl];
        [self .webView loadData:htmlData MIMEType:@"text/html" textEncodingName:@"utf-8" baseURL:baseUrl];
//        [self.webView loadRequest:request];
}

- (IBAction)close:(id)sender {
    [self.presentingViewController dismissViewControllerAnimated:YES completion:nil];
}
@end
