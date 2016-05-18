//
// EvhSampleCommand.m
//
#import "EvhSampleCommand.h"
#import "EvhSampleEmbedded.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSampleCommand
//

@implementation EvhSampleCommand

+(id) withJsonString: (NSString*) jsonString
{
    id jsonObject = [EvhJsonSerializationHelper fromJsonString:jsonString];
    if(jsonObject != nil) {
        EvhSampleCommand* obj = [EvhSampleCommand new];
        return [obj fromJson:jsonObject];
    }
    return nil;
}

-(id) init 
{
    self = [super init];
    if(self) {
        _listValues = [NSMutableArray new];
        _mapValues = [NSMutableDictionary new];
        return self;
    }
    return nil;
}

-(void) toJson: (NSMutableDictionary*) jsonObject 
{
    if(self.command)
        [jsonObject setObject: self.command forKey: @"command"];
    if(self.embedded) {
        NSMutableDictionary* dic = [NSMutableDictionary new];
        [self.embedded toJson: dic];
        
        [jsonObject setObject: dic forKey: @"embedded"];
    }
    if(self.listValues) {
        NSMutableArray* jsonArray = [NSMutableArray new];
        for(EvhSampleEmbedded* item in self.listValues) {
            NSMutableDictionary* dic = [NSMutableDictionary new];
            [item toJson:dic];
            [jsonArray addObject:dic];
        }
        [jsonObject setObject: jsonArray forKey: @"listValues"];
    }
    if(self.mapValues) {
        NSMutableDictionary* jsonMap = [NSMutableDictionary new];
        for(NSString* key in self.mapValues) {
            [jsonMap setValue:[self.mapValues objectForKey: key] forKey: key];
        }
        [jsonObject setObject: jsonMap forKey: @"mapValues"];
    }        
    if(self.sampleEnum)
        [jsonObject setObject: self.sampleEnum forKey: @"sampleEnum"];
    if(self.javaDate)
        [jsonObject setObject: self.javaDate forKey: @"javaDate"];
    if(self.sqlTimestamp)
        [jsonObject setObject: self.sqlTimestamp forKey: @"sqlTimestamp"];
}

-(id<EvhJsonSerializable>) fromJson: (id) jsonObject 
{
    if([jsonObject isKindOfClass:[NSDictionary class]]) {
        self.command = [jsonObject objectForKey: @"command"];
        if(self.command && [self.command isEqual:[NSNull null]])
            self.command = nil;

        NSMutableDictionary* itemJson =  (NSMutableDictionary*)[jsonObject objectForKey: @"embedded"];

        self.embedded = [EvhSampleEmbedded new];
        self.embedded = [self.embedded fromJson: itemJson];
        {
            NSArray* jsonArray = [jsonObject objectForKey: @"listValues"];
            for(id itemJson in jsonArray) {
                EvhSampleEmbedded* item = [EvhSampleEmbedded new];
                
                [item fromJson: itemJson];
                [self.listValues addObject: item];
            }
        }
        {
            NSDictionary* jsonMap = [jsonObject objectForKey: @"mapValues"];
            for(NSString* key in jsonMap) {
                [self.mapValues setObject: [jsonMap objectForKey: key] forKey: key];
            }
        }
        self.sampleEnum = [jsonObject objectForKey: @"sampleEnum"];
        if(self.sampleEnum && [self.sampleEnum isEqual:[NSNull null]])
            self.sampleEnum = nil;

        self.javaDate = [jsonObject objectForKey: @"javaDate"];
        if(self.javaDate && [self.javaDate isEqual:[NSNull null]])
            self.javaDate = nil;

        self.sqlTimestamp = [jsonObject objectForKey: @"sqlTimestamp"];
        if(self.sqlTimestamp && [self.sqlTimestamp isEqual:[NSNull null]])
            self.sqlTimestamp = nil;

        return self;
    }
    
    return nil;
}

@end

///////////////////////////////////////////////////////////////////////////////
