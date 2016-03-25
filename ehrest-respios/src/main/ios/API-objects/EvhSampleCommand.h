//
// EvhSampleCommand.h
// generated at 2016-03-25 09:26:39 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"
#import "EvhSampleEmbedded.h"

///////////////////////////////////////////////////////////////////////////////
// EvhSampleCommand
//
@interface EvhSampleCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* command;

@property(nonatomic, strong) EvhSampleEmbedded* embedded;

// item type EvhSampleEmbedded*
@property(nonatomic, strong) NSMutableArray* listValues;

// item type NSString*
@property(nonatomic, strong) NSMutableDictionary* mapValues;

@property(nonatomic, copy) NSNumber* sampleEnum;

@property(nonatomic, copy) NSNumber* javaDate;

@property(nonatomic, copy) NSNumber* sqlTimestamp;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

