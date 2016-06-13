//
// EvhSampleObject.m
//
#import "EvhSampleObject.h"
#import "EvhSampleEmbedded.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSampleObject
//

@implementation EvhSampleObject

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSampleObject* obj = [EvhSampleObject new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _embeddedList = [NSMutableArray new];
        _embeddedMap = [NSMutableDictionary new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.byteValue)
        [jsonObject setObject: self.byteValue forKey: @"byteValue"];
    if(self.intValue)
        [jsonObject setObject: self.intValue forKey: @"intValue"];
    if(self.longValue)
        [jsonObject setObject: self.longValue forKey: @"longValue"];
    if(self.floatValue)
        [jsonObject setObject: self.floatValue forKey: @"floatValue"];
    if(self.dblValue)
        [jsonObject setObject: self.dblValue forKey: @"dblValue"];
    if(self.javaDate)
        [jsonObject setObject: self.javaDate forKey: @"javaDate"];
    if(self.sqlTimestamp)
        [jsonObject setObject: self.sqlTimestamp forKey: @"sqlTimestamp"];
    if(self.embeddedList) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhSampleEmbedded* item in self.embeddedList) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"embeddedList"];
    }
    if(self.embeddedMap) {
        NSMutableDictionary* jsonMap = [NSMutableDictionary new];
        for(NSString* key in self.embeddedMap) {
            [jsonMap setValue:[self.embeddedMap objectForKey: key] forKey: key];
        }
        [jsonObject setObject: jsonMap forKey: @"embeddedMap"];
    }        
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.byteValue = [jsonObject objectForKey: @"byteValue"];
        if(self.byteValue && [self.byteValue isEqual:[NSNull null]])
            self.byteValue = nil;

        self.intValue = [jsonObject objectForKey: @"intValue"];
        if(self.intValue && [self.intValue isEqual:[NSNull null]])
            self.intValue = nil;

        self.longValue = [jsonObject objectForKey: @"longValue"];
        if(self.longValue && [self.longValue isEqual:[NSNull null]])
            self.longValue = nil;

        self.floatValue = [jsonObject objectForKey: @"floatValue"];
        if(self.floatValue && [self.floatValue isEqual:[NSNull null]])
            self.floatValue = nil;

        self.dblValue = [jsonObject objectForKey: @"dblValue"];
        if(self.dblValue && [self.dblValue isEqual:[NSNull null]])
            self.dblValue = nil;

        self.javaDate = [jsonObject objectForKey: @"javaDate"];
        if(self.javaDate && [self.javaDate isEqual:[NSNull null]])
            self.javaDate = nil;

        self.sqlTimestamp = [jsonObject objectForKey: @"sqlTimestamp"];
        if(self.sqlTimestamp && [self.sqlTimestamp isEqual:[NSNull null]])
            self.sqlTimestamp = nil;

        {
            NSArray* jsonArray = [jsonObject objectForKey: @"embeddedList"];
            for(id itemJson in jsonArray) {
                EvhSampleEmbedded* item = [EvhSampleEmbedded new];
                
                [item fromJson: itemJson];
                [self.embeddedList addObject: item];
            }
        }
        {
            NSDictionary* jsonMap = [jsonObject objectForKey: @"embeddedMap"];
            for(NSString* key in jsonMap) {
                [self.embeddedMap setObject: [jsonMap objectForKey: key] forKey: key];
            }
        }
        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
