//
// EvhRevokeMemberCommand.m
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
//
#import "EvhRevokeMemberCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhRevokeMemberCommand
//

@implementation EvhRevokeMemberCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhRevokeMemberCommand* obj = [EvhRevokeMemberCommand new];
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
    [super toJson: jsonObject];
    if(self.memberUid)
        [jsonObject setObject: self.memberUid forKey: @"memberUid"];
    if(self.reason)
        [jsonObject setObject: self.reason forKey: @"reason"];
    if(self.operatorRole)
        [jsonObject setObject: self.operatorRole forKey: @"operatorRole"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        [super fromJson: jsonObject];
        self.memberUid = [jsonObject objectForKey: @"memberUid"];
        if(self.memberUid && [self.memberUid isEqual:[NSNull null]])
            self.memberUid = nil;

        self.reason = [jsonObject objectForKey: @"reason"];
        if(self.reason && [self.reason isEqual:[NSNull null]])
            self.reason = nil;

        self.operatorRole = [jsonObject objectForKey: @"operatorRole"];
        if(self.operatorRole && [self.operatorRole isEqual:[NSNull null]])
            self.operatorRole = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
