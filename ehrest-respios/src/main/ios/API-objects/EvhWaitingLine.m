//
// EvhWaitingLine.m
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
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
