//
//  UIViewController+AboutViewController.m
//  DragGame
//
//  Created by 黯然星海 on 2021/12/30.
//  Copyright © 2021年 anranxinghai. All rights reserved.
//

#import "UIViewController+AboutViewController.h"

@implementation AboutViewController

- (IBAction)close:(id)sender {
    [self.presentingViewController dismissViewControllerAnimated:YES completion:nil];
}
@end
