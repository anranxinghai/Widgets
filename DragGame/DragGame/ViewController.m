//
//  ViewController.m
//  DragGame
//
//  Created by 黯然星海 on 2021/12/26.
//  Copyright © 2021年 anranxinghai. All rights reserved.
//

#import "ViewController.h"

@interface ViewController (){
    int curValue;
}
- (IBAction)showAlert:(id)sender;
- (IBAction)slideMoved:(UISlider *)sender;
@property (strong, nonatomic) IBOutlet UISlider *slider;

@end

@implementation ViewController
@synthesize slider;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    curValue = self.slider.value;
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (IBAction)showAlert:(id)sender {
    NSString *message = [NSString stringWithFormat:@"当前程度值是%d",curValue];
    [[[UIAlertView alloc]initWithTitle:@"您好，苍老师" message:message delegate:nil cancelButtonTitle:@"我来阅览一下" otherButtonTitles:nil, nil] show];
}

- (IBAction)slideMoved:(UISlider *)sender {
    slider = (UISlider *)sender;
    NSLog(@"当前滑竿值是%f",slider.value);
    curValue = lround(slider.value);
}
@end
