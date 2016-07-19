//
// EvhGetNewsDetailInfoCommand.m
//
#import "EvhGetNewsDetailInfoCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetNewsDetailInfoCommand
//

@implementation EvhGetNewsDetailInfoCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetNewsDetailInfoCommand* obj = [EvhGetNewsDetailInfoCommand new];
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
    if(self.theNewsToken)
        [jsonObject setObject: self.theNewsToken forKey: @"newsToken"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.theNewsToken = [jsonObject objectForKey: @"newsToken"];
        if(self.theNewsToken && [self.theNewsToken isEqual:[NSNull null]])
            self.theNewsToken = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
