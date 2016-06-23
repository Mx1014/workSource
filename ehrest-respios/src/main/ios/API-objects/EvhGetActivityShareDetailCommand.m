//
// EvhGetActivityShareDetailCommand.m
//
#import "EvhGetActivityShareDetailCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetActivityShareDetailCommand
//

@implementation EvhGetActivityShareDetailCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetActivityShareDetailCommand* obj = [EvhGetActivityShareDetailCommand new];
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
    if(self.postToken)
        [jsonObject setObject: self.postToken forKey: @"postToken"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.postToken = [jsonObject objectForKey: @"postToken"];
        if(self.postToken && [self.postToken isEqual:[NSNull null]])
            self.postToken = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
