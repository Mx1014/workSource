//
// EvhWaitingLine.m
//
#import "EvhWaitingLine.h"

///////////////////////////////////////////////////////////////////////////////
// EvhWaitingLine
//

@implementation EvhWaitingLine

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhWaitingLine* obj = [EvhWaitingLine new];
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
    if(self.waitingPeople)
        [jsonObject setObject: self.waitingPeople forKey: @"waitingPeople"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.waitingPeople = [jsonObject objectForKey: @"waitingPeople"];
        if(self.waitingPeople && [self.waitingPeople isEqual:[NSNull null]])
            self.waitingPeople = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
