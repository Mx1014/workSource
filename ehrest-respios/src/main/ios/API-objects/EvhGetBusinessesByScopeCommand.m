//
// EvhGetBusinessesByScopeCommand.m
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:52 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:55 
>>>>>>> 3.3.x
//
#import "EvhGetBusinessesByScopeCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhGetBusinessesByScopeCommand
//

@implementation EvhGetBusinessesByScopeCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhGetBusinessesByScopeCommand* obj = [EvhGetBusinessesByScopeCommand new];
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
    if(self.scopeType)
        [jsonObject setObject: self.scopeType forKey: @"scopeType"];
    if(self.scopeId)
        [jsonObject setObject: self.scopeId forKey: @"scopeId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.scopeType = [jsonObject objectForKey: @"scopeType"];
        if(self.scopeType && [self.scopeType isEqual:[NSNull null]])
            self.scopeType = nil;

        self.scopeId = [jsonObject objectForKey: @"scopeId"];
        if(self.scopeId && [self.scopeId isEqual:[NSNull null]])
            self.scopeId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
