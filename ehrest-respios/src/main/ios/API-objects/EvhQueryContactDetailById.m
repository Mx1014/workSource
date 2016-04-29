//
// EvhQueryContactDetailById.m
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:57 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:54 
>>>>>>> 3.3.x
//
#import "EvhQueryContactDetailById.h"

///////////////////////////////////////////////////////////////////////////////
// EvhQueryContactDetailById
//

@implementation EvhQueryContactDetailById

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhQueryContactDetailById* obj = [EvhQueryContactDetailById new];
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
    if(self.contactId)
        [jsonObject setObject: self.contactId forKey: @"contactId"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.contactId = [jsonObject objectForKey: @"contactId"];
        if(self.contactId && [self.contactId isEqual:[NSNull null]])
            self.contactId = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
