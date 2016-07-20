//
// EvhBusinessCommand.m
//
#import "EvhBusinessCommand.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBusinessCommand
//

@implementation EvhBusinessCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhBusinessCommand* obj = [EvhBusinessCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _categroies = [NSMutableArray new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.targetType)
        [jsonObject setObject: self.targetType forKey: @"targetType"];
    if(self.targetId)
        [jsonObject setObject: self.targetId forKey: @"targetId"];
    if(self.bizOwnerUid)
        [jsonObject setObject: self.bizOwnerUid forKey: @"bizOwnerUid"];
    if(self.name)
        [jsonObject setObject: self.name forKey: @"name"];
    if(self.displayName)
        [jsonObject setObject: self.displayName forKey: @"displayName"];
    if(self.logoUri)
        [jsonObject setObject: self.logoUri forKey: @"logoUri"];
    if(self.url)
        [jsonObject setObject: self.url forKey: @"url"];
    if(self.contact)
        [jsonObject setObject: self.contact forKey: @"contact"];
    if(self.phone)
        [jsonObject setObject: self.phone forKey: @"phone"];
    if(self.longitude)
        [jsonObject setObject: self.longitude forKey: @"longitude"];
    if(self.latitude)
        [jsonObject setObject: self.latitude forKey: @"latitude"];
    if(self.address)
        [jsonObject setObject: self.address forKey: @"address"];
    if(self.description_)
        [jsonObject setObject: self.description_ forKey: @"description"];
    if(self.categroies) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(NSNumber* item in self.categroies) {
            [jsonArray addObject:item];
        }
        [jsonObject setObject: jsonArray forKey: @"categroies"];
    }
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.targetType = [jsonObject objectForKey: @"targetType"];
        if(self.targetType && [self.targetType isEqual:[NSNull null]])
            self.targetType = nil;

        self.targetId = [jsonObject objectForKey: @"targetId"];
        if(self.targetId && [self.targetId isEqual:[NSNull null]])
            self.targetId = nil;

        self.bizOwnerUid = [jsonObject objectForKey: @"bizOwnerUid"];
        if(self.bizOwnerUid && [self.bizOwnerUid isEqual:[NSNull null]])
            self.bizOwnerUid = nil;

        self.name = [jsonObject objectForKey: @"name"];
        if(self.name && [self.name isEqual:[NSNull null]])
            self.name = nil;

        self.displayName = [jsonObject objectForKey: @"displayName"];
        if(self.displayName && [self.displayName isEqual:[NSNull null]])
            self.displayName = nil;

        self.logoUri = [jsonObject objectForKey: @"logoUri"];
        if(self.logoUri && [self.logoUri isEqual:[NSNull null]])
            self.logoUri = nil;

        self.url = [jsonObject objectForKey: @"url"];
        if(self.url && [self.url isEqual:[NSNull null]])
            self.url = nil;

        self.contact = [jsonObject objectForKey: @"contact"];
        if(self.contact && [self.contact isEqual:[NSNull null]])
            self.contact = nil;

        self.phone = [jsonObject objectForKey: @"phone"];
        if(self.phone && [self.phone isEqual:[NSNull null]])
            self.phone = nil;

        self.longitude = [jsonObject objectForKey: @"longitude"];
        if(self.longitude && [self.longitude isEqual:[NSNull null]])
            self.longitude = nil;

        self.latitude = [jsonObject objectForKey: @"latitude"];
        if(self.latitude && [self.latitude isEqual:[NSNull null]])
            self.latitude = nil;

        self.address = [jsonObject objectForKey: @"address"];
        if(self.address && [self.address isEqual:[NSNull null]])
            self.address = nil;

        self.description_ = [jsonObject objectForKey: @"description"];
        if(self.description_ && [self.description_ isEqual:[NSNull null]])
            self.description_ = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"categroies"];
            for(id itemJson in jsonArray) {
                [self.categroies addObject: itemJson];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
