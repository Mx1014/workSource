//
// EvhSampleCommand.h
<<<<<<< HEAD
<<<<<<< HEAD
// generated at 2016-04-18 14:48:51 
=======
// generated at 2016-04-19 14:25:56 
>>>>>>> 3.3.x
=======
// generated at 2016-04-26 18:22:56 
>>>>>>> 3.3.x
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

