//
// EvhBannerClickDTO.m
//
#import "EvhBannerClickDTO.h"

///////////////////////////////////////////////////////////////////////////////
// EvhBannerClickDTO
//

@implementation EvhBannerClickDTO

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhBannerClickDTO* obj = [EvhBannerClickDTO new];
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
    if(self.id)
        [jsonObject setObject: self.id forKey: @"id"];
    if(self.uuid)
        [jsonObject setObject: self.uuid forKey: @"uuid"];
    if(self.bannerId)
        [jsonObject setObject: self.bannerId forKey: @"bannerId"];
    if(self.uid)
        [jsonObject setObject: self.uid forKey: @"uid"];
    if(self.familyId)
        [jsonObject setObject: self.familyId forKey: @"familyId"];
    if(self.clickCount)
        [jsonObject setObject: self.clickCount forKey: @"clickCount"];
    if(self.lastClickTime)
        [jsonObject setObject: self.lastClickTime forKey: @"lastClickTime"];
    if(self.createTime)
        [jsonObject setObject: self.createTime forKey: @"createTime"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.id = [jsonObject objectForKey: @"id"];
        if(self.id && [self.id isEqual:[NSNull null]])
            self.id = nil;

        self.uuid = [jsonObject objectForKey: @"uuid"];
        if(self.uuid && [self.uuid isEqual:[NSNull null]])
            self.uuid = nil;

        self.bannerId = [jsonObject objectForKey: @"bannerId"];
        if(self.bannerId && [self.bannerId isEqual:[NSNull null]])
            self.bannerId = nil;

        self.uid = [jsonObject objectForKey: @"uid"];
        if(self.uid && [self.uid isEqual:[NSNull null]])
            self.uid = nil;

        self.familyId = [jsonObject objectForKey: @"familyId"];
        if(self.familyId && [self.familyId isEqual:[NSNull null]])
            self.familyId = nil;

        self.clickCount = [jsonObject objectForKey: @"clickCount"];
        if(self.clickCount && [self.clickCount isEqual:[NSNull null]])
            self.clickCount = nil;

        self.lastClickTime = [jsonObject objectForKey: @"lastClickTime"];
        if(self.lastClickTime && [self.lastClickTime isEqual:[NSNull null]])
            self.lastClickTime = nil;

        self.createTime = [jsonObject objectForKey: @"createTime"];
        if(self.createTime && [self.createTime isEqual:[NSNull null]])
            self.createTime = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
