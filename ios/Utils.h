#ifndef Utils_h
#define Utils_h

@interface Utils : NSObject
+ (BOOL)isVisible:(UIView *)view inView:(UIView *)inView minPercentage:(CGFloat) minPercentage;
+ (BOOL) intersects: (CGRect) rect1 andRect:(CGRect) rect2 minPercentage:(CGFloat) minPercentage;
+ (CGFloat) getIntersectionSize: (CGRect) rect1 andRect:(CGRect) rect2;
@end

#endif /* Utils_h */
