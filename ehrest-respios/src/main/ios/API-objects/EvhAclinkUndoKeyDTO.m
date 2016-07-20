//
// EvhAclinkUndoKeyDTO.m
//
#import "EvhAclinkUndoKeyDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhAclinkUndoKeyDTO
//

@implementation EvhAclinkUndoKeyDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhAclinkUndoKeyDTO* obj = [EvhAclinkUndoKeyDTO new];
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
    if(self.status)
        [jsonObject setObject: self.status forKey: @"status"];
    if(self.keyId)
        [jsonObject setObject: self.keyId forKey: @"keyId"];
    if(self.createTimeMs)
        [jsonObject setObject: self.createTimeMs forKey: @"createTimeMs"];
    if(self.expireTimeMs)
        [jsonObject setObject: self.expireTimeMs forKey: @"expireTimeMs"];
    if(self.doorId)
        [jsonObject setObject: self.doorId forKey: @"doorId"];
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.status = [jsonObject objectForKey: @"status"];
        if(self.status && [self.status isEqual:[NSNull null]])
            self.status = nil;

        self.keyId = [jsonObject objectForKey: @"keyId"];
        if(self.keyId && [self.keyId isEqual:[NSNull null]])
            self.keyId = nil;

        self.createTimeMs = [jsonObject objectForKey: @"createTimeMs"];
        if(self.createTimeMs && [self.createTimeMs isEqual:[NSNull null]])
            self.createTimeMs = nil;

        self.expireTimeMs = [jsonObject objectForKey: @"expireTimeMs"];
        if(self.expireTimeMs && [self.expireTimeMs isEqual:[NSNull null]])
            self.expireTimeMs = nil;

        self.doorId = [jsonObject objectForKey: @"doorId"];
        if(self.doorId && [self.doorId isEqual:[NSNull null]])
            self.doorId = nil;

        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
