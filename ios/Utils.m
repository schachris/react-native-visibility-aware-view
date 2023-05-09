#import <Foundation/Foundation.h>
#import "Utils.h"
#import <UIKit/UIKit.h>

@implementation Utils

+ (id)alloc {
  [NSException raise:@"Cannot be instantiated!" format:@"Static class 'Utils' cannot be instantiated!"];
  return nil;
}


+ (BOOL)isVisible:(UIView *)view inView:(UIView *)inView minPercentage:(CGFloat) minPercentage {
    if (view.isHidden || view.alpha == 0) {
        return NO;
    }
    if (!inView) {
        return YES;
    }
    CGRect viewFrame = [inView convertRect:view.bounds fromView:view];
    if ([Utils intersects:viewFrame andRect:inView.bounds minPercentage:minPercentage]) {
        return [Utils isVisible:view inView:inView.superview minPercentage:minPercentage];
    }
    return NO;
}


+ (BOOL) intersects: (CGRect) rect1 andRect:(CGRect) rect2 minPercentage:(CGFloat) minPercentage {
    CGFloat intersectionSize = [Utils getIntersectionSize:rect1 andRect:rect2];
    CGFloat size =  rect1.size.width * rect1.size.height;
    BOOL intersects = CGRectIntersectsRect(rect1, rect2);
    CGFloat percentage =  intersectionSize / size;
    BOOL isEnoughVisible = percentage > minPercentage;
    return intersects && isEnoughVisible;
}

+ (CGFloat) getIntersectionSize: (CGRect) rect1 andRect:(CGRect) rect2 {
    CGRect overlappingRect = CGRectIntersection(rect2, rect1);
    if (!CGRectIsNull(overlappingRect)) {
        CGFloat overlappingArea = overlappingRect.size.width * overlappingRect.size.height;
        return  overlappingArea;
    } else {
        return 0;
    }
}

@end
