//
// EvhSetMinimumMonthsCommand.m
// generated at 2016-03-31 19:08:53 
//
#import "EvhSetMinimumMonthsCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSetMinimumMonthsCommand
//

@implementation EvhSetMinimumMonthsCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSetMinimumMonthsCommand* obj = [EvhSetMinimumMonthsCommand new];
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
    if(self.months)
        [jsonObject setObject: self.months forKey: @"months"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.months = [jsonObject objectForKey: @"months"];
        if(self.months && [self.months isEqual:[NSNull null]])
            self.months = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
