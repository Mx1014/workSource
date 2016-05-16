//
// EvhCountAccountOrdersAndMonths.m
//
#import "EvhCountAccountOrdersAndMonths.h"

///////////////////////////////////////////////////////////////////////////////
// EvhCountAccountOrdersAndMonths
//

@implementation EvhCountAccountOrdersAndMonths

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhCountAccountOrdersAndMonths* obj = [EvhCountAccountOrdersAndMonths new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.orders)
        [jsonObject setObject: self.orders forKey: @"orders"];
    if(self.months)
        [jsonObject setObject: self.months forKey: @"months"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.orders = [jsonObject objectForKey: @"orders"];
        if(self.orders && [self.orders isEqual:[NSNull null]])
            self.orders = nil;

        self.months = [jsonObject objectForKey: @"months"];
        if(self.months && [self.months isEqual:[NSNull null]])
            self.months = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
