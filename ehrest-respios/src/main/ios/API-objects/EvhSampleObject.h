//
// EvhSampleObject.h
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhSampleEmbedded.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSampleObject
//
@interface EvhSampleObject
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSNumber* byteValue;

@property(nonatomic, copy) NSNumber* intValue;

@property(nonatomic, copy) NSNumber* longValue;

@property(nonatomic, copy) NSNumber* floatValue;

@property(nonatomic, copy) NSNumber* dblValue;

@property(nonatomic, copy) NSNumber* javaDate;

@property(nonatomic, copy) NSNumber* sqlTimestamp;

// item type EvhSampleEmbedded*
@property(nonatomic, strong) NSMutableArray* embeddedList;

// item type NSString*
@property(nonatomic, strong) NSMutableDictionary* embeddedMap;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

