//
//  ViewController.m
//  DragGame
//
//  Created by 黯然星海 on 2021/12/26.
//  Copyright © 2021年 anranxinghai. All rights reserved.
//

#import "ViewController.h"

@interface ViewController(){
    int curValue;
    int targetValue;
    int totalScore;
    int round;
}
- (IBAction)showAlert:(id)sender;
- (IBAction)slideMoved:(UISlider *)sender;
@property (strong, nonatomic) IBOutlet UISlider *slider;
@property (strong, nonatomic) IBOutlet UILabel *roundLabel;
@property (strong, nonatomic) IBOutlet UITextField *targetLabel;
@property (strong, nonatomic) IBOutlet UILabel *totalScoreLabel;
- (IBAction)startOver:(id)sender;
-(void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex;
@end

@implementation ViewController
@synthesize slider;
@synthesize targetLabel;
@synthesize roundLabel;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    [self startNewGame];
    
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void) startNewRound{
    targetValue = 1+(arc4random()%100);
    curValue = 50;
    round += 1;
    self.slider.value = curValue;
    self.targetLabel.text = [NSString stringWithFormat:@"%d", curValue];
    self.roundLabel.text = [NSString stringWithFormat:@"%d",round];
}

-(void) updateLabel{
    self.targetLabel.text =  [NSString stringWithFormat:@"%d", curValue];
}

- (void)startNewGame{
    totalScore = 0;
    round = 0;
    [self startNewRound];
}

- (IBAction)startOver:(id)sender {
    [self startNewGame];
    [self updateLabel];
}


-(void)alertView:(UIAlertView *)alertView didDismissWithButtonIndex:(NSInteger)buttonIndex{
    [self updateTotalScore];
    [self startNewRound];
}

-(void) updateTotalScore{
    totalScore += [self cumputerScore];
    NSString * totalScoreString = [NSString stringWithFormat:@"%d",totalScore];
    self.totalScoreLabel.text = totalScoreString;
}

-(int) cumputerScore{
    return 100 - abs(curValue - targetValue);
}

- (IBAction)showAlert:(id)sender {
    int curScore = [self cumputerScore];
    NSString *message = [NSString stringWithFormat:@"当前程度值是%d,我们的目标数值是%d,得分%d",curValue,targetValue,curScore];
    [[[UIAlertView alloc]initWithTitle:@"您好，苍老师" message:message delegate:self cancelButtonTitle:@"本老师知道了" otherButtonTitles:nil, nil] show];
    
    
}

- (IBAction)slideMoved:(UISlider *)sender {
    slider = (UISlider *)sender;
    NSLog(@"当前滑竿值是%f",slider.value);
    curValue = lround(slider.value);
    [self updateLabel];
}
@end
