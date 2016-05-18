//
// EvhExtendedSourceAccountPeriodCommand.m
//
#import "EvhExtendedSourceAccountPeriodCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhExtendedSourceAccountPeriodCommand
//

@implementation EvhExtendedSourceAccountPeriodCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhExtendedSourceAccountPeriodCommand* obj = [EvhExtendedSourceAccountPeriodCommand new];
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
    if(self.sourceAccountId)
        [jsonObject setObject: self.sourceAccountId forKey: @"sourceAccountId"];
    if(self.validDate)
        [jsonObject setObject: self.validDate forKey: @"validDate"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.sourceAccountId = [jsonObject objectForKey: @"sourceAccountId"];
        if(self.sourceAccountId && [self.sourceAccountId isEqual:[NSNull null]])
            self.sourceAccountId = nil;

        self.validDate = [jsonObject objectForKey: @"validDate"];
        if(self.validDate && [self.validDate isEqual:[NSNull null]])
            self.validDate = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
