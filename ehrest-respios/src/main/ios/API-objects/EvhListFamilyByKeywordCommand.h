//
// EvhListFamilyByKeywordCommand.h
// generated at 2016-04-06 19:10:43 
//
#import <Foundation/Foundation.h>
#import "JsonSerializable.h"

///////////////////////////////////////////////////////////////////////////////
// EvhListFamilyByKeywordCommand
//
@interface EvhListFamilyByKeywordCommand
    : NSObject<EvhJsonSerializable>


@property(nonatomic, copy) NSString* keyword;

-(id) init;
+(id) withJsonString: (NSString*) jsonString;

@end

///////////////////////////////////////////////////////////////////////////////

